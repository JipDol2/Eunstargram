const addSignUpSocialEvent = () =>{
    const signUpSocialBtn = document.getElementById("singUp-social-form-submit");
    signUpSocialBtn.addEventListener("click",signUpSocialOperation);

    const connectEmailToSocialBtn = document.getElementById("connectEmailToSocial");
    connectEmailToSocialBtn.addEventListener("click",connectEmailToSocial);
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
        document.getElementById("validationCheckEmail").style.display = 'block';
    }
}

const connectEmailToSocial = async () =>{
    const data ={
        "email" : document.getElementById("id").value
    };

    const header={
        method: 'POST',
        body: JSON.stringify(data)
    };

    const response = await fetchData("/api/member/email/social",header);
    location.href = location.origin+"/login";
}
addSignUpSocialEvent();