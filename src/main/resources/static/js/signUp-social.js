const addSignUpSocialEvent = () =>{
    const signUpSocialButton = document.getElementById("singUp-social-form-submit");
    signUpSocialButton.addEventListener("click",signUpSocialOperation);
}

const signUpSocialOperation = async () =>{
    const data = {
        "email" : document.getElementById("id").value
    };

    const header = {
        method: 'POST',
        body: JSON.stringify(data)
    };

    const response = await fetchData("/api/member/validation/email",header);
    console.log(response.flag);
    if(response.flag===false){

    }else{

    }
}
addSignUpSocialEvent();