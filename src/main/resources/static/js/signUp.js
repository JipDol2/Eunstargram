const addSingUpEvent = () =>{
    const signUpFormSubmit = document.getElementById("singUp-form-submit");
    signUpFormSubmit.addEventListener("click",singUpOperation);
};

const singUpOperation = async (event) => {
    event.preventDefault();

    const singUpDTO = {
        memberId: document.getElementById("id").value,
        password: document.getElementById("password").value,
        nickname: document.getElementById("nickname").value,
        phoneNumber: document.getElementById("phoneNumber").value,
        birthDay: document.getElementById("birthDay").value
    };

    const header = {
        method: "POST",
        body: JSON.stringify(singUpDTO)
    };

    try{
        const response = await fetchData("/api/member/signUp",header);
        location.href = location.origin+"/";
    }catch(e){
        alert("입력한 정보를 다시 확인해주세요");
    }
}
addSingUpEvent();