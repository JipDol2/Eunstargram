const addIndexEvent = () =>{
    validationLogin();

    const findMemoryButton = document.getElementById("findMemoryButton");
    findMemoryButton.addEventListener("click",clickPostsOperation);

    const logoutButtonOperation = document.getElementById("logout-button");
    logoutButtonOperation.addEventListener("click",clickLogOutOperation);
}

const validationLogin = async () => {
    // const token = sessionStorage.getItem("Authorization");

    const header = {
        method: 'GET'
    };

    try {
        const response = await fetchData("/api/auth/checkAuth", header);

        if(response.authState == true) {
            const logoutState = document.getElementById("logoutState");
            logoutState.classList.toggle("d-none");

            const loginState = document.getElementById("loginState");
            loginState.classList.toggle("d-none");
        }

    }catch (e){

    }

}

const clickPostsOperation = async (event) => {

    const header={
        method: 'GET'
    };
    try{
        const response = await fetchData(`/api/member/findByMember`,header);
        location.href = `${location.origin}/posts/${response.id}`;
    }catch (e){
        location.reload();
    }

}

const clickLogOutOperation = async (event) => {

    const header = {
        method: 'POST'
    };

    const response = await fetchData(`/api/auth/logout`,header);
    location.href = location.origin+"/";
}
addIndexEvent();