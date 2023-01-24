const addPostsEvent = () => {
    const uploadBox = document.getElementById("uploadBox");
    uploadBox.addEventListener("click",uploadImage)
}
const uploadImage = (event) => {
    const uploadProfile = document.getElementById("profileImage");
    uploadProfile.click();
}
addPostsEvent();