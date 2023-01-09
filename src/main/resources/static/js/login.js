const addLoginEvent = () => {
  const loginFormSubmit = document.getElementById("login-form-submit");
  loginFormSubmit.addEventListener("click",loginOperation);
};

const loginOperation = async (event) =>{
    event.preventDefault();

    const loginDTO = {
        memberId : document.getElementById("id").value,
        password : document.getElementById("password").value
    };
    const header = {
        method : 'POST',
        body : JSON.stringify(loginDTO)
    };

    try{
        const response = await fetchData("/api/member/login",header);
        location.href = location.origin+"/";
    }catch(e){
        const errorMessage = document.getElementById("error-message");
        errorMessage.innerHTML = "아이디 혹은 비밀번호를 잘못 입력하셨습니다.";
    }
};

addLoginEvent();