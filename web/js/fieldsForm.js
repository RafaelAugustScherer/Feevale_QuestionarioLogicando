import { getFirestore, collection, addDoc, serverTimestamp, Timestamp } from 'https://www.gstatic.com/firebasejs/11.6.0/firebase-firestore.js';

const db = getFirestore(globalThis.app);

const QUESTION_TYPES = {
    TEXT: 'text',
    SINGLE: 'single',
    MULTIPLE: 'multiple'
};

const SELECTOR_TYPES = {
    TYPE: 0,
    TEXT: 1,
    CHOICE: 2
};

const fieldsForm = document.getElementById("fields-form");

const getComponentRefById = (id) => document.getElementById(id);

const getQuantityOfQuestions = () => fieldsForm.querySelector('fieldset').querySelectorAll('fieldset').length;

const getSelectorByType = (type, questionNumber, choiceNumber) => {
    if(type === SELECTOR_TYPES.TYPE) {
        return `question-${questionNumber}-type`;
    }
    if(type === SELECTOR_TYPES.TEXT) {
        return `question-${questionNumber}-text`;
    }
    if(type === SELECTOR_TYPES.CHOICE) {
        return `question-${questionNumber}-choice-${choiceNumber}`;
    }
    return 'Invalid selector options';
};

const createInputStructure = ({ uniqueId, name, shouldWrap, required }) => {
    const inputElement = Object.assign(document.createElement('input'), { id: uniqueId, name: name ?? uniqueId, required });
    const labelElement = document.createElement('label');
    labelElement.setAttribute('for', uniqueId);

    if(shouldWrap) {
        const wrapper = document.createElement('div');
        wrapper.append(labelElement, inputElement);

        return { labelElement, inputElement, wrapper };
    }
    return { labelElement, inputElement };
};

const addChoice = (questionNumber, choiceNumber, choicesContainer, onRemove) => {
    const { inputElement, labelElement } = createInputStructure({ uniqueId: getSelectorByType(SELECTOR_TYPES.CHOICE, questionNumber, choiceNumber), required: true });

    labelElement.textContent = `Opção ${choiceNumber}:`;

    inputElement.setAttribute('type', 'text');
    inputElement.setAttribute('class', 'question-choice');

    choicesContainer.appendChild(labelElement);

    if(onRemove) {
        const wrapper = document.createElement('div');
        wrapper.className = 'choice-row';

        const removeButton = document.createElement('span');
        removeButton.textContent = '🗑️';
        removeButton.className = 'remove-choice-button';
        removeButton.addEventListener('click', () => {
            labelElement.remove();
            wrapper.remove();
            onRemove();
        });

        wrapper.appendChild(inputElement);
        wrapper.appendChild(removeButton);
        choicesContainer.appendChild(wrapper);
        return;
    }

    choicesContainer.appendChild(inputElement);
};

const makeQuestionComponent = () => {
    const thisQuestionNumber = getQuantityOfQuestions() + 1;

    const container = document.createElement('div');
    container.className = 'question-container';

    const handleAddQuestionContentByType = (questionType) => {
        const questionContent = document.createElement('div');

        const currentContainerRef = getComponentRefById('fields-form').querySelectorAll('fieldset > fieldset').item(thisQuestionNumber - 1);
        const isAlreadyPopulated = currentContainerRef.querySelector('input[type=text]') !== null;
        if(isAlreadyPopulated) {
            container.removeChild(container.lastChild);
        }

        const { inputElement, labelElement } = createInputStructure({ uniqueId: getSelectorByType(SELECTOR_TYPES.TEXT, thisQuestionNumber), required: true });

        labelElement.textContent = 'Texto da Pergunta:';
        inputElement.setAttribute('type', 'text');
        inputElement.setAttribute('class', 'question-text');

        questionContent.appendChild(labelElement);
        questionContent.appendChild(inputElement);

        const choicesContainer = document.createElement('div');

        if([QUESTION_TYPES.SINGLE, QUESTION_TYPES.MULTIPLE].includes(questionType)) {
            const addChoiceButton = document.createElement('button');
            addChoiceButton.textContent = 'Adicionar Opção';
            addChoiceButton.type = 'button';

            let choicesCount = 0;

            for(let i = 0; i < 2; i++) {
                choicesCount++;
                addChoice(thisQuestionNumber, choicesCount, choicesContainer);
            }

            addChoiceButton.addEventListener('click', () => {
                choicesCount++;
                addChoice(thisQuestionNumber, choicesCount, choicesContainer, () => {
                    choicesCount--;
                    addChoiceButton.classList.remove('hidden');
                });

                if(choicesCount >= 6) {
                    addChoiceButton.classList.add('hidden');
                }
            });

            questionContent.appendChild(choicesContainer);
            questionContent.appendChild(addChoiceButton);
        }

        container.appendChild(questionContent);
    };

    const typeContainer = document.createElement('div');

    const createRadioChoice = (questionType, label) => {
        const { labelElement, inputElement } = createInputStructure({
            uniqueId: `question-${thisQuestionNumber}-type-${questionType}`,
            name: getSelectorByType(SELECTOR_TYPES.TYPE, thisQuestionNumber),
            required: true
        });

        labelElement.textContent = label;

        inputElement.setAttribute('type', 'radio');
        inputElement.setAttribute('value', questionType);
        inputElement.setAttribute('class', `input-type`);
        inputElement.addEventListener('change', () => handleAddQuestionContentByType(questionType));

        typeContainer.appendChild(inputElement);
        typeContainer.appendChild(labelElement);
    };

    createRadioChoice(QUESTION_TYPES.TEXT, 'Texto');
    createRadioChoice(QUESTION_TYPES.SINGLE, 'Escolha Única');
    createRadioChoice(QUESTION_TYPES.MULTIPLE, 'Escolha Múltipla');

    container.appendChild(typeContainer);

    return container;
};

const addQuestionToFieldsForm = () => {
    const thisQuestionNumber = getQuantityOfQuestions() + 1;

    const fieldset = document.createElement('fieldset');
    fieldset.appendChild(Object.assign(document.createElement('legend'), { textContent: `Pergunta ${thisQuestionNumber}` }));

    const removeButton = document.createElement('span');
    removeButton.textContent = '🗑️';
    removeButton.className = 'remove-question-button';
    removeButton.addEventListener('click', () => removeQuestionFromFieldsForm(fieldset));

    fieldset.append(removeButton, makeQuestionComponent());
    fieldsForm.querySelector('fieldset').appendChild(fieldset);
};

const removeQuestionFromFieldsForm = (fieldsetElement) => {
    if(getQuantityOfQuestions() > 1) {
        fieldsetElement.remove();

        getComponentRefById('fields-form').querySelector('fieldset').querySelectorAll('fieldset').forEach((fieldset, index) => {
            const questionPosition = index + 1;

            fieldset.querySelector('legend').textContent = `Pergunta ${questionPosition}`;

            fieldset.querySelectorAll('.input-type').forEach(inputTypeSelector => inputTypeSelector.setAttribute('name', getSelectorByType(SELECTOR_TYPES.TYPE, questionPosition)));

            fieldset.querySelector('.question-text')?.setAttribute('name', `question-${questionPosition}-text`);
            fieldset.querySelectorAll('.question-choice').forEach((element, key) => {
                element.setAttribute('name', getSelectorByType(SELECTOR_TYPES.CHOICE, questionPosition, key + 1));
            });
        });
    }
};

// FORM CREATION
fieldsForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    if(!globalThis.user) {
        console.error('Unable to create form since current user is not authenticated!');
        return;
    }

    const formData = new FormData(fieldsForm);

    const numberOfQuestions = getQuantityOfQuestions();

    const questionsPayload = [];

    for(let questionNumber = 1; questionNumber <= numberOfQuestions; questionNumber++) {
        const questionType = formData.get(getSelectorByType(SELECTOR_TYPES.TYPE, questionNumber));
        const questionText = formData.get(getSelectorByType(SELECTOR_TYPES.TEXT, questionNumber));

        questionsPayload[questionNumber - 1] = {
            type: questionType,
            text: questionText,
        };

        if([QUESTION_TYPES.SINGLE, QUESTION_TYPES.MULTIPLE].includes(questionType)) {
            questionsPayload[questionNumber - 1].choices = [];


            Array.from({ length: 6 }, (_, i) => i + 1).forEach((choiceNumber) => {
                const choiceText = formData.get(getSelectorByType(SELECTOR_TYPES.CHOICE, questionNumber, choiceNumber));
                if(choiceText) {
                    questionsPayload[questionNumber - 1].choices.push({
                        choiceId: choiceNumber,
                        text: choiceText
                    });
                }
            });
        }
    }

    addDoc(collection(db, '/forms'), {
        name: formData.get('name'),
        dateAvailableFrom: new Date(formData.get('date-available-from')),
        dateAvailableUntil: new Date(formData.get('date-available-until')),
        questions: questionsPayload,
        userId: globalThis.user.uid,
        createdAt: serverTimestamp()
    })
        .then(() => {
            fieldsForm.querySelector('.error').remove();
            const successSpan = fieldsForm.appendChild(Object.assign(document.createElement('span'), { textContent: 'Questionário criado com sucesso!', className: 'success' }));
            setTimeout(() => {
                successSpan.remove();
            }, 5000);
        })
        .catch(error => {
            console.error(error);
            fieldsForm.querySelector('.error').remove();
            loginForm.firstElementChild.appendChild(Object.assign(document.createElement('span'), { textContent: 'Houve um erro ao criar o questionário', className: 'error' }));
        });
});

window.addQuestionToFieldsForm = addQuestionToFieldsForm;

// INITIALIZE
addQuestionToFieldsForm();
fieldsForm.classList.add('disabled');
