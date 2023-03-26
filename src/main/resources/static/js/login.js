const addLoginEvent = () => {
  const loginFormSubmit = document.getElementById("login-form-submit");
  loginFormSubmit.addEventListener("click",loginOperation);
};

const loginOperation = async (event) =>{
    event.preventDefault();

    try{
        const loginDTO = {
            memberEmail : document.getElementById("id").value,
            password : document.getElementById("password").value
        };
        const header = {
            method : 'POST',
            body : JSON.stringify(loginDTO)
        };

        const response = await fetchData("/api/auth/login",header);
    }catch(e){
        const errorMessage = document.getElementById("error-message");
        errorMessage.innerHTML = "아이디 혹은 비밀번호를 잘못 입력하셨습니다.";
    }

    try{
        const header={
            method: 'GET',
            body: null
        };
        const response = await fetchData("/api/member/findByMember",header);
        sessionStorage.setItem("Id",response.id);
        // console.log(response);
        location.href = location.origin+"/";
    }catch(e){

    }


};

addLoginEvent();