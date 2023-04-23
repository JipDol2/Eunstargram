const addIndexEvent = () =>{
    validationLogin();

    const findMemoryButton = document.getElementById("findMemoryButton");
    findMemoryButton.addEventListener("click",clickPostsOperation);

    const logoutButtonOperation = document.getElementById("logout-button");
    logoutButtonOperation.addEventListener("click",clickLogOutOperation);
}
/**
 * login 유효성 체크
 */
const validationLogin = async () => {
    const header = {
        method: 'GET'
    };

    try {
        const response = await fetchData("/api/auth/loginCheck", header);

        if(response.authState == true) {
            const logoutState = document.getElementById("logoutState");
            logoutState.classList.toggle("d-none");

            const loginState = document.getElementById("loginState");
            loginState.classList.toggle("d-none");
        }

    }catch (e){

    }

}
/**
 * 추억 탐험하기 버튼 클릭 -> 게시글 조회 이동
 */
const clickPostsOperation = async (event) => {
    const header={
        method: 'GET'
    };
    try{
        const response = await fetchData(`/api/member/findByMyInfo`,header);
        location.href = location.origin + `/posts/${response.nickname}`;
    }catch (e){
        location.reload();
    }
}
/**
 * 로그아웃
 */
const clickLogOutOperation = async (event) => {

    const header = {
        method: 'POST'
    };

    const response = await fetchData(`/api/auth/logout`,header);
    location.href = location.origin+"/";
}
addIndexEvent();