const addIndexEvent = () =>{
    validationLogin();
}

const validationLogin = () =>{
    const token = sessionStorage.getItem("Id");
    if(token){
        const logoutState = document.getElementById("logoutState");
        logoutState.classList.toggle("d-none");

        const loginState = document.getElementById("loginState");
        loginState.classList.toggle("d-none");
    }
}

const clickPostsOperation = async (event) =>{
    location.href = location.origin+"/posts";
}
addIndexEvent();