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
}

//TODO: 이미지를 동적으로 추가한 뒤 그 이미지를 클릭했을 경우 모달창이 나와야 하는데.... 어떻게 구현?

/**
 * 로그인 후 게시글 페이지 로딩
 * @param event
 * @returns {Promise<void>}
 */
const getPosts = async(event) => {

    const memberId = sessionStorage.getItem("Id");

    const header = {
        method: 'GET'
    }

    try{
        const response = await fetchData('/api/post/'+memberId,header);

        const root = document.querySelector('.mylist_contents');

        for (const element of response) {
            const divTag = document.createElement("div");
            divTag.className='pic';

            const button = document.createElement("button");
            button.type = "button";
            button.class = "btn";
            // button.name = `postBtn`;
            // button.id = `btn_${element.imageDTO.id}`;
            button.setAttribute("data-bs-toggle","modal");
            button.setAttribute("data-bs-target","#imageModal");

            const img = document.createElement("img");
            // console.log(element.imageDTO.storedFileName)
            img.src = `/upload/${element.imageResponseDTO.storedFileName}`;
            img.id = element.id;    //postId

            /**
             * Q. button click 시 event.target 이 왜 button 에 대한 정보가 아닌걸까?
             * A. 정답은 event bubbling (https://ko.javascript.info/bubbling-and-capturing) 때문이다.
             *    클릭은 image 를 클릭한 것이 되고 click event 가 event bubbling 때문에 button 에게도 올라간다.
             *    이때 button click 시 listener 를 등록해놓았기 때문데 event.target 에는 image 에 관한 정보가 담겨져있다.
             */
            button.addEventListener("click", async (event) => {
                const imageContent = document.getElementById("modalImage");
                /**
                 * hidden input 을 하나 두어서 거기에 postId 값을 담음
                 */
                const hiddenInput = document.getElementsByName("postIdInput");
                const postId = event.target.id;

                imageContent.src = event.target.src;
                hiddenInput.id = postId;   //postId

                // console.log(hiddenInput.id);
                /**
                 * 게시글 및 댓글 조회
                 * - 직접 html 코드들을 생성해서 append 시켜줌
                 */
                const sectionCheck = document.getElementById("scroll_section");
                if(sectionCheck){
                    sectionCheck.remove();
                }

                const section = document.createElement("section");
                section.setAttribute("class","scroll_section");
                section.setAttribute("id","scroll_section");

                const postContent = await findByPost(element.id);
                section.appendChild(postContent);
                section.appendChild(document.createElement("hr"));

                const arr = await findByComments(element.id);
                arr.forEach(element=>section.appendChild(element));

                const box = document.getElementById("detail--right_box");
                box.prepend(section);
            });
            button.appendChild(img);
            divTag.appendChild(button);
            root.appendChild(divTag);
        }
    }catch (e){
        throw new Error("잘못된 게시글 목록 요청입니다.");
    }
}

/**
 * 프로필 사진 업로드 버튼 클릭시 myFile input 클릭 이벤트 실행
 * @param event
 * @returns {Promise<void>}
 */
const uploadImage = async (event) => {
    event.preventDefault();
    const buttonId = event.target.id;
    if(buttonId==="myFile"){
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

        /**
         * TODO: 이미지 업로드 이후 modal 창 닫기 그리고 프로필 사진 변경 적용
         * TODO: fetch 를 사용해서 multipart-form server 로 전송 방법 블로그에 글 쓰기
         * TODO: https://stackoverflow.com/questions/35192841/how-do-i-post-with-multipart-form-data-using-fetch
         */
    }catch(e){

    }
    location.reload();
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
    // formData.append("likeNumber",0);
    formData.append("content",document.getElementById("content").value);
    formData.append("memberId",1);
    formData.append("image",image.files[0]);

    const header={
        method:'POST',
        body:formData
    };

    try{
        const response = await fetchFileData('/api/post/upload',header);
    }catch(e){

    }
    location.reload();
}

/**
 * 게시글 조회
 * @param postId
 * @returns {Promise<void>}
 */
const findByPost = async (postId) => {
    const header = {
        method: 'GET'
    };
    const response = await fetchData(`/api/post/p/${postId}`,header);

    const admin_container = document.createElement("div");
    admin_container.setAttribute("class","comment");

    const user_id = document.createElement("span");
    user_id.setAttribute("class","user_id");
    user_id.innerText = `${response.nickname} `;

    const content = document.createElement("span");
    content.innerText = response.content;

    admin_container.appendChild(user_id);
    admin_container.appendChild(content);

    return admin_container;
}

/**
 * 댓글 조회
 */
const findByComments = async (postId) =>{
    const header = {
        method: 'GET'
    };
    const response = await fetchData(`/api/comment/${postId}`,header);
    // console.log(response);

    const arr=[];
    response.forEach(element=>{
        const container = document.createElement("div");
        container.setAttribute("class","user_container-detail");
        container.setAttribute("id","user_container-detail")

        const comment = document.createElement("div");
        comment.setAttribute("id","comment");
        comment.setAttribute("class","comment");

        const user_id = document.createElement("span");
        user_id.setAttribute("class","user_id");
        user_id.innerText = `${element.nickname} `

        const content = document.createElement("span");
        content.innerText = element.content;

        comment.appendChild(user_id);
        comment.appendChild(content);
        container.appendChild(comment);
        arr.push(container);
    });
    return arr;
}

//TODO : 댓글 저장 버튼 클릭시 댓글란 초기화
/**
 * 댓글 저장
 * @param event
 * @returns {Promise<void>}
 */
const saveComment = async (event)=>{
    event.preventDefault();

    try{
        let header = {
            method: 'GET'
        }
        let response = await fetchData("/api/member/findByMember",header);

        const param={
            content : document.getElementById("inputComment").value,
            postId : document.getElementsByName("postIdInput").id,
            memberId : response.id
        };

        header={
            method: 'POST',
            body: JSON.stringify(param)
        }
        response = await fetchData("/api/comment/upload",header);

    }catch(e){

    }
}
getPosts();
addPostsEvent();