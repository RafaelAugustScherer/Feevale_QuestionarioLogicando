import { getFirestore, collection, addDoc, serverTimestamp } from 'https://www.gstatic.com/firebasejs/11.6.0/firebase-firestore.js';

const db = getFirestore(globalThis.app);

const QUESTION_TYPES = {
    TEXT: 'text',
    SINGLE: 'single',
    MULTIPLE: 'multiple'
};

const SELECTOR_TYPES = {
    TYPE: 0,
    TEXT: 1,
    OPTION: 2
};

const fieldsForm = document.getElementById("fields-form");

const getComponentRefById = (id) => document.getElementById(id);

const getQuantityOfQuestions = () => fieldsForm.querySelector('fieldset').querySelectorAll('fieldset').length;

const getSeletorByType = (type, questionNumber, optionNumber) => {
    if(type === SELECTOR_TYPES.TYPE) {
        return `question-${questionNumber}-type`;
    }
    if(type === SELECTOR_TYPES.TEXT) {
        return `question-${questionNumber}-text`;
    }
    if(type === SELECTOR_TYPES.OPTION) {
        return `question-${questionNumber}-option-${optionNumber}`;
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

const addOption = (questionNumber, optionNumber, optionsContainer, onRemove) => {
    const { inputElement, labelElement } = createInputStructure({ uniqueId: getSeletorByType(SELECTOR_TYPES.OPTION, questionNumber, optionNumber), required: true });

    labelElement.textContent = `Opção ${optionNumber}:`;

    inputElement.setAttribute('type', 'text');
    inputElement.setAttribute('class', 'question-option');

    optionsContainer.appendChild(labelElement);

    if(onRemove) {
        const wrapper = document.createElement('div');
        wrapper.className = 'option-row';

        const removeButton = document.createElement('span');
        removeButton.textContent = '🗑️';
        removeButton.className = 'remove-option-button';
        removeButton.addEventListener('click', () => {
            labelElement.remove();
            wrapper.remove();
            onRemove();
        });

        wrapper.appendChild(inputElement);
        wrapper.appendChild(removeButton);
        optionsContainer.appendChild(wrapper);
        return;
    }

    optionsContainer.appendChild(inputElement);
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

        const { inputElement, labelElement } = createInputStructure({ uniqueId: getSeletorByType(SELECTOR_TYPES.TEXT, thisQuestionNumber), required: true });

        labelElement.textContent = 'Texto da Pergunta:';
        inputElement.setAttribute('type', 'text');
        inputElement.setAttribute('class', 'question-text');

        questionContent.appendChild(labelElement);
        questionContent.appendChild(inputElement);

        const optionsContainer = document.createElement('div');

        if([QUESTION_TYPES.SINGLE, QUESTION_TYPES.MULTIPLE].includes(questionType)) {
            const addOptionButton = document.createElement('button');
            addOptionButton.textContent = 'Adicionar Opção';
            addOptionButton.type = 'button';

            let optionsCount = 0;

            for(let i = 0; i < 2; i++) {
                optionsCount++;
                addOption(thisQuestionNumber, optionsCount, optionsContainer);
            }

            addOptionButton.addEventListener('click', () => {
                optionsCount++;
                addOption(thisQuestionNumber, optionsCount, optionsContainer, () => {
                    optionsCount--;
                    addOptionButton.classList.remove('hidden');
                });

                if(optionsCount >= 6) {
                    addOptionButton.classList.add('hidden');
                }
            });

            questionContent.appendChild(optionsContainer);
            questionContent.appendChild(addOptionButton);
        }

        container.appendChild(questionContent);
    };

    const typeContainer = document.createElement('div');

    const createRadioOption = (questionType, label) => {
        const { labelElement, inputElement } = createInputStructure({
            uniqueId: `question-${thisQuestionNumber}-type-${questionType}`,
            name: getSeletorByType(SELECTOR_TYPES.TYPE, thisQuestionNumber),
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

    createRadioOption(QUESTION_TYPES.TEXT, 'Texto');
    createRadioOption(QUESTION_TYPES.SINGLE, 'Escolha Única');
    createRadioOption(QUESTION_TYPES.MULTIPLE, 'Escolha Múltipla');

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

            fieldset.querySelectorAll('.input-type').forEach(inputTypeSelector => inputTypeSelector.setAttribute('name', getSeletorByType(SELECTOR_TYPES.TYPE, questionPosition)));

            fieldset.querySelector('.question-text')?.setAttribute('name', `question-${questionPosition}-text`);
            fieldset.querySelectorAll('.question-option').forEach((element, key) => {
                element.setAttribute('name', getSeletorByType(SELECTOR_TYPES.OPTION, questionPosition, key + 1));
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
        const questionType = formData.get(getSeletorByType(SELECTOR_TYPES.TYPE, questionNumber));
        const questionText = formData.get(getSeletorByType(SELECTOR_TYPES.TEXT, questionNumber));

        questionsPayload[questionNumber - 1] = {
            type: questionType,
            text: questionText,
        };

        if([QUESTION_TYPES.SINGLE, QUESTION_TYPES.MULTIPLE].includes(questionType)) {
            questionsPayload[questionNumber - 1].options = [];


            Array.from({ length: 6 }, (_, i) => i + 1).forEach((optionNumber) => {
                const optionText = formData.get(getSeletorByType(SELECTOR_TYPES.OPTION, questionNumber, optionNumber));
                if(optionText) {
                    questionsPayload[questionNumber - 1].options.push(optionText);
                }
            });
        }
    }

    console.log({ name: 'Teste', questions: questionsPayload, userId: globalThis.user.uid });

    addDoc(collection(db, '/forms'), {
        name: 'Teste',
        questions: questionsPayload,
        userId: globalThis.user.uid,
        createdAt: serverTimestamp()
    })
        .catch(error => console.error(error));

    // addDoc(collection(db, `/users/${globalThis.user.uid}/forms`), { name: 'Teste', questions: questionsPayload })
    //     .catch(error => console.error(error));
});

window.addQuestionToFieldsForm = addQuestionToFieldsForm;

// INITIALIZE
addQuestionToFieldsForm();
fieldsForm.classList.add('disabled');
