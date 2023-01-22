const addIndexEvent = () =>{
    validationLogin();

    const postsOperation = document.getElementById("posts-button");
    postsOperation.addEventListener("click",clickPostsOperation);
}
const validationLogin = () =>{
    const token = localStorage.getItem("Authorization");
    if(token){
        const logoutState = document.getElementById("logoutState");
        logoutState.classList.toggle("d-none");

        const loginState = document.getElementById("loginState");
        loginState.classList.toggle("d-none");
    }
}
const clickPostsOperation = async (event) =>{

}
addIndexEvent();