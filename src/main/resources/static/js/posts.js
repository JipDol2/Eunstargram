const addPostsEvent = () => {
    const openImgUpload  = document.getElementById("openImgUpload");
    openImgUpload.addEventListener("click",uploadImage);

    const saveFileForm = document.getElementById("saveFile");
    saveFileForm.addEventListener("click",saveFile);
}

const uploadImage = async (event) => {
    event.preventDefault();
    document.getElementById("myFile").click();
}

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

    try{
        const response = await fetchFileData('/api/member/uploadProfileImage',options);
        /**
         * TODO: 이미지 업로드 이후 modal 창 닫기 그리고 프로필 사진 변경 적용
         * TODO: fetch 를 사용해서 multipart-form server 로 전송 방법 블로그에 글 쓰기
         * TODO: https://stackoverflow.com/questions/35192841/how-do-i-post-with-multipart-form-data-using-fetch
         */
    }catch(e){

    }
}

addPostsEvent();