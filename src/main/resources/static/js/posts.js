const addPostsEvent = () => {
    const openImgUpload  = document.getElementById("openImgUpload");
    openImgUpload.addEventListener("click",uploadImage);

    const saveFileForm = document.getElementById("saveFile");
    saveFileForm.addEventListener("click",saveFile);
}

//TODO : 111111순위다 이게 일본다녀와서 제일 먼저 해야할 일
//TODO : post.html 에 접속했을때 프로필 정보와 게시글 정보들을 가져오는 로직 작성

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

addPostsEvent();