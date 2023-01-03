const addLoginEvent = () => {
  const loginFormSubmit = document.getElementById("login-form-submit");
  loginFormSubmit.addEventListener("click",loginOperation);
};

const loginOperation = (event) =>{
    event.preventDefault();
    alert("click button");
};

addLoginEvent();