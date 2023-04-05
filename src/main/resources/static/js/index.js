const addIndexEvent = () =>{
    validationLogin();

    const findMemoryButton = document.getElementById("findMemoryButton");
    findMemoryButton.addEventListener("click",clickPostsOperation);

}

const validationLogin = () => {
    const token = sessionStorage.getItem("Authorization");
    if(token){
        const logoutState = document.getElementById("logoutState");
        logoutState.classList.toggle("d-none");

        const loginState = document.getElementById("loginState");
        loginState.classList.toggle("d-none");
    }
}

const clickPostsOperation = async (event) => {

    const header={
        method: 'GET'
    };

    const response = await fetchData(`/api/member/findByMember`,header);
    location.href = `${location.origin}/posts/${response.id}`;
}

addIndexEvent();