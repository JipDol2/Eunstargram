const addPostsEvent = () => {

    const profileInfo = getProfileInfo();

    const saveFileForm = document.getElementById("saveFile");
    saveFileForm.addEventListener("click",saveFile);

    const savePostsForm = document.getElementById("savePosts");
    savePosts.addEventListener("click",saveFile);
}

const getProfileInfo = async(event) => {

    const memberId = 1;

    const options = {
        method: 'GET'
    }

    try{
        const response = await fetchData('/api/post/'+memberId,options);
        console.log(response);
    }catch (e){

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

    for(let value of formData.values()){
        console.log(value);
    }
    const options = {
        method:'POST',
        body: formData
    };

    let response=null;

    try{
        response = await fetchFileData('/api/member/profileImage',options);
        console.log(response.storedFileName);
        console.log(response.originalFileName);

        document.getElementById("profileImage").src = '/upload/' + response.storedFileName;

        /**
         * TODO: 이미지 업로드 이후 modal 창 닫기 그리고 프로필 사진 변경 적용
         * TODO: fetch 를 사용해서 multipart-form server 로 전송 방법 블로그에 글 쓰기
         * TODO: https://stackoverflow.com/questions/35192841/how-do-i-post-with-multipart-form-data-using-fetch
         */
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

    const requestPostDTO = {
        likeNumber : 0,
        content : document.getElementById("content").value,
        memberId : 1,
        image : image
    };

    const formData = new FormData();
    formData.append("requestPostDTO",requestPostDTO);

    console.log(`requestPostDTO : ${requestPostDTO}`);

    const options={
        method:'POST',
        body:formData
    };

    try{
        const response = await fetchFileData('/api/post/upload',options);
        for(let i=0;i<response.size;i++){
            console.log(response.likeNumber[i]);
        }
    }catch(e){

    }
}

addPostsEvent();