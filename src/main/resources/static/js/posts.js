const addPostsEvent = () => {

    const openImgUpload = document.getElementById("openImgUpload");
    openImgUpload.addEventListener("click",uploadImage);

    const saveFileForm = document.getElementById("saveFile");
    saveFileForm.addEventListener("click",saveFile);

    const openPostUpload = document.getElementById("openPostUpload");
    openPostUpload.addEventListener("click",uploadImage);

    const savePostsForm = document.getElementById("savePosts");
    savePostsForm.addEventListener("click",savePosts);
}

//TODO: 이미지를 동적으로 추가한 뒤 그 이미지를 클릭했을 경우 모달창이 나와야 하는데.... 어떻게 구현?

/**
 * 로그인 후 게시글 페이지 로딩
 * @param event
 * @returns {Promise<void>}
 */
const getProfileInfo = async(event) => {

    const memberId = sessionStorage.getItem("Id");

    const options = {
        method: 'GET'
    }

    try{
        const response = await fetchData('/api/post/'+memberId,options);

        const root = document.querySelector('.mylist_contents');

        response.forEach(element=>{
            const divTag = document.createElement("div");
            divTag.className='pic';

            // const aTag = document.createElement("a");
            // aTag.href = '#';

            const button = document.createElement("button");
            button.type = "button";
            button.class = "btn";
            button.setAttribute("data-bs-toggle","modal");
            button.setAttribute("data-bs-target","#imageModal");

            const img = document.createElement("img");
            // console.log(element.imageDTO.storedFileName)
            img.src = `/upload/${element.imageDTO.storedFileName}`;
            img.id = element.imageDTO.id;

            button.appendChild(img);
            divTag.appendChild(button);
            root.appendChild(divTag);
        });
        console.log(root);
    }catch (e){

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

    // for(let value of formData.values()){
    //     console.log(value);
    // }
    const options = {
        method:'POST',
        body: formData
    };

    let response=null;

    try{
        response = await fetchFileData('/api/member/profileImage',options);
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

    const options={
        method:'POST',
        body:formData
    };

    try{
        const response = await fetchFileData('/api/post/upload',options);
    }catch(e){

    }
    location.reload();
}
getProfileInfo();
addPostsEvent();