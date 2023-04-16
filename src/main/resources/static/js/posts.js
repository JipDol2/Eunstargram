const addPostsEvent = () => {

    const openImgUpload = document.getElementById("openImgUpload");
    openImgUpload.addEventListener("click",uploadImage);

    const saveFileForm = document.getElementById("saveFile");
    saveFileForm.addEventListener("click",saveFile);

    const openPostUpload = document.getElementById("openPostUpload");
    openPostUpload.addEventListener("click",uploadImage);

    const savePostsForm = document.getElementById("savePosts");
    savePostsForm.addEventListener("click",savePosts);

    const uploadComment = document.getElementById("uploadComment");
    uploadComment.addEventListener("click",saveComment);

    const logOutButton = document.getElementById("logout-button");
    logOutButton.addEventListener("click",clickLogOutOperation);

    const postDeleteButton = document.getElementById("postDeleteButton");
    postDeleteButton.addEventListener("click",clickPostDeleteOperation);

    const searchButton = document.getElementById("search-button");
    searchButton.addEventListener("click",clickSearchOperation);
}

/**
 * 프로필 이미지 조회
 */
const getProfileImage = async (event) =>{

    let nickname = null;
    try{
        const header = {
            method: 'GET'
        };
        const response = await fetchData(`/api/member/findByMyInfo`,header);
        nickname = response.nickname;
    }catch (error){
        location.href = location.origin + "/";
    }

    document.getElementById("user_name").innerText = nickname;
    document.getElementById("nick_name").innerText = nickname;

    try{
        const header = {
            method: 'GET'
        };
        const response = await fetchData(`/api/member/profileImage/${nickname}`);
        document.getElementById("profileImage").src = `/upload/${response.storedFileName}`;
    }catch (e){
        document.getElementById("profileImage").src = `/upload/fox.jpg`;
        // location.href = location.origin + "/";
    }
}

/**
 * 로그인 후 게시글 페이지 로딩
 * @param event
 * @returns {Promise<void>}
 */
const getPosts = async(searchNickname) => {

    let nickname = null;
    try{
        const header = {
            method: 'GET'
        }
        if(searchNickname == null) {
            const response = await fetchData(`/api/member/findByMyInfo`, header);
            nickname = response.nickname;
        }else{
            const response = await fetchData(`/api/member/findByMember/${searchNickname}`, header);
            nickname = response.nickname;
        }
    }catch (error){
        location.href = location.origin + "/";
    }

    try{
        const header = {
            method: 'GET',
        }

        const response = await fetchData(`/api/post/${nickname}`,header);
        document.getElementById("postNumber").innerText = response.data.length;
        // console.log(response.length);

        const root = document.querySelector('.mylist_contents');

        for (const element of response.data) {
            const divTag = document.createElement("div");
            divTag.className='pic';

            const img = document.createElement("img");
            // console.log(element.imageDTO.storedFileName)
            img.setAttribute("class","img");
            img.setAttribute("data-bs-toggle","modal");
            img.setAttribute("data-bs-target","#imageModal");
            img.src = `/upload/${element.imageResponseDTO.storedFileName}`;
            img.id = element.id;    //postId

            /**
             * Q. button click 시 event.target 이 왜 button 에 대한 정보가 아닌걸까?
             * A. 정답은 event bubbling (https://ko.javascript.info/bubbling-and-capturing) 때문이다.
             *    클릭은 image 를 클릭한 것이 되고 click event 가 event bubbling 때문에 button 에게도 올라간다.
             *    이때 button click 시 listener 를 등록해놓았기 때문데 event.target 에는 image 에 관한 정보가 담겨져있다.
             */
            img.addEventListener("click", async (event) => {
                const imageContent = document.getElementById("modalImage");
                /**
                 * hidden input 을 하나 두어서 거기에 postId 값을 담음
                 */
                const hiddenInput = document.getElementsByName("postIdInput")[0];
                const postId = event.target.id;

                imageContent.src = event.target.src;
                hiddenInput.id = postId;   //postId
                hiddenInput.value = postId;

                // console.log(hiddenInput.id);
                /**
                 * 게시글 및 댓글 조회
                 * - 직접 html 코드들을 생성해서 append 시켜줌
                 */
                makeModal(postId);
            });
            divTag.appendChild(img);
            root.prepend(divTag);
        }
    }catch (e){
        location.href = location.origin + "/";
    }
}
/**
 * 모달창 생성
 * @param element
 * @returns {Promise<void>}
 */
const makeModal = async (postId) => {

    const box = document.getElementById("detail--right_box");
    const section = document.getElementById("scroll_section");
    /**
     *  게시글 조회
     */
    const postContent = await findByPost(postId);
    /**
     * 댓글 조회
     */
    const arr = await findByComments(postId);
    box.prepend(section);
}
/**
 * 프로필 사진 업로드 버튼 클릭시 myFile input 클릭 이벤트 실행
 * @param event
 * @returns {Promise<void>}
 */
const uploadImage = async (event) => {
    event.preventDefault();
    const buttonId = event.target.id;
    if(buttonId==="openImgUpload"){
        document.getElementById("myFile").click();
    }else{
        document.getElementById("contentFile").click();
    }
}

/**
 * 이미지 파일 저장
 * @param event
 * @returns {Promise<void>}
 */
const saveFile = async (event) =>{
    event.preventDefault();
    const image = document.getElementById("myFile");

    const formData = new FormData();
    formData.append('image',image.files[0]);

    const header = {
        method:'POST',
        body: formData
    };

    let response=null;

    try{
        response = await fetchFileData('/api/member/profileImage',header);
        // console.log(response.storedFileName);
        // console.log(response.originalFileName);
        document.getElementById("profileImage").src = `/upload/${response.storedFileName}`;
    }catch(e){

    }
}

/**
 * 게시글 업로드
 * @param event
 * @returns {Promise<void>}
 */
const savePosts = async(event)=>{
    event.preventDefault();
    const image = document.getElementById("contentFile");

    const formData = new FormData();
    formData.append("content",document.getElementById("content").value);
    formData.append("image",image.files[0]);

    try{
        const header={
            method:'POST',
            body:formData
        };
        const response = await fetchFileData('/api/post/upload',header);
    }catch(e){

    }
    location.reload();
}
/**
 * 모달창 게시글 조회
 * @param postId
 * @returns {Promise<HTMLElement>}
 */
const findByPost = async (postId) => {

    const header = {
        method: 'GET'
    };
    const response = await fetchData(`/api/post/p/${postId}`,header);

    const user_id = document.getElementById("user_id");
    user_id.innerHTML = `${response.nickname}`;

    const content = document.getElementById("post_content");
    content.innerHTML = `${response.content}`;

    const deletePostTarget = document.getElementById("deleteTargetPostId");
    deletePostTarget.data = postId;
}

/**
 * 모달창 댓글 조회
 * @param postId
 * @returns {Promise<*[]>}
 */
const findByComments = async (postId) =>{
    const header = {
        method: 'GET'
    };
    const response = await fetchData(`/api/comment/${postId}`,header);
    // console.log(response);

    const commentBox = document.getElementById("commentBox");
    commentBox.innerHTML='';

    // const arr=[];
    response.data.forEach(element=>{
        const container = document.createElement("div");
        container.className="user_container-detail";
        container.id="user_container-detail";

        const htmlCode = `            
            <div class="comment" id="comment">
                <span class="user_id">${element.nickname}</span>
                <span style="font-size: small">${element.content}</span>
            </div>
            <div class="sprite_more_icon" data-name="more"></div>            
        `;
        container.innerHTML=htmlCode;
        // arr.push(container);
        commentBox.appendChild(container);
    });

}

/**
 * 댓글 저장
 * @param event
 * @returns {Promise<void>}
 */
const saveComment = async (event)=>{
    event.preventDefault();

    try{
        const param={
            content: document.getElementById("inputComment").value,
            postId: document.getElementsByName("postIdInput")[0].value
        };
        const header={
            method: 'POST',
            body: JSON.stringify(param)
        }
        const response = await fetchData("/api/comment/upload",header);
        const inputBox = document.getElementById("inputComment");
        inputBox.value = null;
        //TODO: 게시 버튼 클릭 후 다시 모달창이 reload 되어야 하지만 현재 modal 창의 location 은 post 이다....
        //TODO: 어떻게 해결을 해야될지 잘 모르겠음
        location.reload();
    }catch(e){

    }
}

/**
 * 로그아웃
 * @param event
 * @returns {Promise<void>}
 */
const clickLogOutOperation = async (event) => {

    const header={
        method: 'POST'
    };

    try{
        const response = await fetchData(`/api/auth/logout`,header);
        sessionStorage.clear();
    }catch(e){
        throw new Error("로그아웃 실패");
    }
    location.href = location.origin+"/";
}

const clickPostDeleteOperation = async (event) => {
    const header = {
        method: 'DELETE'
    };
    const postId = document.getElementById("deleteTargetPostId").data;
    console.log(postId);

    try{
        const response = await fetchData(`/api/post/p/delete/${postId}`,header);
    }catch (e){

    }
}

const clickSearchOperation = async(event) => {
    const header = {
        method: 'GET'
    };

    const searchNickname = document.getElementById("search-nickname").value;

    try{
        const response = await fetchData(`/api/member/findByMember/${searchNickname}`);

        getPosts(response.nickname);
    }catch(e){

    }
}

getProfileImage();
getPosts();
addPostsEvent();