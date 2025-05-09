import { getAuth, signInWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/11.6.0/firebase-auth.js";

const auth = getAuth(globalThis.app);

const fieldsForm = document.getElementById("fields-form");
const loginForm = document.getElementById("login-form");

auth.onAuthStateChanged(async (user) => {
    if(user) {
        globalThis.user = user;
        document.getElementById('email').setAttribute('value', user.email);

        console.log(loginForm.firstElementChild.childElementCount);
        loginForm.firstElementChild.appendChild(Object.assign(document.createElement('span'), { textContent: 'Usuário autenticado com sucesso!', className: 'success' }));
        fieldsForm.classList.remove('disabled'); // ENABLE FORM AFTER AUTHENTICATION
    }
});

loginForm.addEventListener("submit", e => {
    e.preventDefault();

    const formData = new FormData(loginForm);

    signInWithEmailAndPassword(auth, formData.get("email"), formData.get("password"))
        .then(userCredential => {
            const user = userCredential.user;
            globalThis.user = user;
            console.log(user);

            loginForm.firstElementChild.appendChild(Object.assign(document.createElement('span'), { textContent: 'Usuário autenticado com sucesso!', className: 'success' }));
            fieldsForm.classList.remove('disabled'); // ENABLE FORM AFTER AUTHENTICATION
        })
        .catch(error => {
            console.log(`Login error: Error with code ${error.code} and message ${error.message} `);

            let errorMessageText = 'Houve um erro no processo de autenticação.';

            if(error.code == 'auth/invalid-credential') {
                errorMessageText = 'Credenciais de acesso inválidas.';
            }

            loginForm.firstElementChild.appendChild(Object.assign(document.createElement('span'), { textContent: errorMessageText, className: 'error' }));
        });
});
