const addIndexEvent = () =>{
    validationLogin();

    // const postsOperation = document.getElementById("posts-button");
    // postsOperation.addEventListener("click",clickPostsOperation);
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

    //TODO: 토큰에 사용자의 정보를 넣을 수 있는가?아니면 session 을 사용해야되나?


    location.href = location.origin+"/posts";
}
addIndexEvent();