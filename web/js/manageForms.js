import {
  getFirestore,
  collection,
  doc,
  getDocs,
  getDoc,
  query,
  where,
  getCountFromServer,
  writeBatch,
} from 'https://www.gstatic.com/firebasejs/11.6.0/firebase-firestore.js';
import { getAuth } from 'https://www.gstatic.com/firebasejs/11.6.0/firebase-auth.js';


const db = getFirestore(globalThis.app);
const auth = getAuth(globalThis.app);

const dateFmt = new Intl.DateTimeFormat('pt-BR');
function formatPeriod({ dateAvailableFrom, dateAvailableUntil }) {
  const fmt = d => (d ? dateFmt.format(d) : '—');
  return `${fmt(dateAvailableFrom)} → ${fmt(dateAvailableUntil)}`;
}

function createIconButton(label, title) {
  const btn = document.createElement('button');
  btn.type = 'button';
  btn.title = title;
  btn.textContent = label;
  return btn;
}

auth.onAuthStateChanged(async user => {
  if(!user) return;

  const container = document.getElementById('manage-forms');
  if(!container) {
    return;
  }

  container.innerHTML = '<p>Carregando…</p>';
  const formsInfo = await getFormsInfo();
  container.innerHTML = '';

  if(!formsInfo.length) {
    container.innerHTML = '<p>Nenhum formulário encontrado.</p>';
    return;
  }

  formsInfo.forEach(info => {
    // card
    const card = document.createElement('div');
    card.className = 'form-card';

    // bloc‑informações
    const infoBlock = document.createElement('div');
    const titleEl = document.createElement('h3');
    titleEl.textContent = info.name;

    const periodEl = document.createElement('p');
    periodEl.textContent = formatPeriod(info);

    const answersEl = document.createElement('p');
    answersEl.textContent = `${info.answersCount} resposta(s)`;

    infoBlock.append(titleEl, periodEl, answersEl);

    const actions = document.createElement('div');

    const downloadBtn = createIconButton('⬇️', 'Baixar CSV de respostas');
    downloadBtn.addEventListener('click', () => downloadFormAnswersCSV(info.id));

    const deleteBtn = createIconButton('🗑️', 'Excluir formulário');
    deleteBtn.addEventListener('click', async () => {
      if(
        confirm(
          `Tem certeza que deseja excluir o formulário "${info.name}"?\n` +
          'Esta ação é permanente e removerá todas as respostas.'
        )
      ) {
        await deleteForm(info.id);
        card.remove();
      }
    });

    actions.append(downloadBtn, deleteBtn);
    card.append(infoBlock, actions);
    container.appendChild(card);
  });
});

export async function getFormsInfo() {
  const user = auth.currentUser;
  if(!user) return [];

  const formsQuery = query(collection(db, 'forms'), where('userId', '==', user.uid));
  const snap = await getDocs(formsQuery);

  const out = [];
  for(const docSnap of snap.docs) {
    const ansQuery = query(collection(db, 'forms', docSnap.id, 'answers'));
    const ansAgg = await getCountFromServer(ansQuery);
    const data = docSnap.data();
    out.push({
      id: docSnap.id,
      name: data.name ?? 'Sem título',
      dateAvailableFrom: data.dateAvailableFrom?.toDate?.() ?? null,
      dateAvailableUntil: data.dateAvailableUntil?.toDate?.() ?? null,
      answersCount: ansAgg.data().count,
    });
  }
  return out;
}

const stringifyAnswer = (val) => {
  if(val == null) return '';

  if(Array.isArray(val)) {
    if(val.every(v => typeof v !== 'object')) return val.join('; ');

    if(val.every(v => typeof v === 'object' && 'text' in v)) return val.map(v => v.text).join('; ');
    return JSON.stringify(val);
  }

  if(typeof val === 'object') {
    if('text' in val) return val.text;
    if('answer' in val) return stringifyAnswer(val.answer);
    if('value' in val) return stringifyAnswer(val.value);
    return JSON.stringify(val);
  }

  if(val instanceof Date) return val.toISOString();

  return String(val);
};

export async function downloadFormAnswersCSV(formId) {
  const formSnap = await getDoc(doc(db, 'forms', formId));
  if(!formSnap.exists()) throw new Error('Formulário não encontrado');
  const formData = formSnap.data();
  const questionsArr = formData.questions ?? [];

  const qMap = new Map(questionsArr.map(q => [q.questionId, q.text]));

  const ansSnap = await getDocs(collection(db, 'forms', formId, 'answers'));

  const fixedHeaders = ['ID_resposta', 'Usuário'];
  const questionHeaders = questionsArr.map(q => q.text);
  const headers = [...fixedHeaders, ...questionHeaders];

  const rows = [];
  ansSnap.forEach(d => {
    const data = d.data();

    const row = {
      ID_resposta: d.id,
      Usuário: data.userName ?? '',
    };

    const answersBlock = data.answers ?? [];

    if(Array.isArray(answersBlock)) {
      answersBlock.forEach(a => {
        const question = questionsArr.find(question => question.questionId == a.questionId);
        const questionText = question.text;
        const questionChoices = question.choices;

        let value = a.selectedChoice ?? a.selectedChoices ?? a.answer;

        if(typeof value === 'number') {
          console.log(questionChoices);
          value = questionChoices?.find(choice => choice.choiceId == value).text;
        }
        if(typeof value === 'object') {
          value = questionChoices?.filter(choice => value.includes(choice.choiceId)).map(choice => choice.text).join(', ');
        }

        row[questionText] = stringifyAnswer(value);
      });
    }

    rows.push(row);
  });

  const csvLines = [headers.map(h => `"${h.replace(/"/g, '""')}"`).join(',')];

  rows.forEach(r => {
    const line = headers.map(h => {
      const val = r[h] ?? '';
      return `"${String(val).replace(/"/g, '""')}"`;
    }).join(',');
    csvLines.push(line);
  });

  const blob = new Blob([csvLines.join('\n')], { type: 'text/csv;charset=utf-8;' });
  const link = Object.assign(document.createElement('a'), {
    href: URL.createObjectURL(blob),
    download: `${(formData?.name ?? 'form').replace(/[^a-z0-9_-]/gi, '_')}-answers.csv`,
  });
  document.body.appendChild(link);
  link.click();
}

export async function deleteForm(formId) {
  const batch = writeBatch(db);
  const answersCol = collection(db, 'forms', formId, 'answers');
  const answersSnap = await getDocs(answersCol);
  answersSnap.forEach(d => batch.delete(d.ref));
  batch.delete(doc(db, 'forms', formId));

  await batch.commit();
}
