const REQUEST_URL = 'http://localhost:8080';

const fetchData = async (url, option) => {
    try {
        const res = await fetch(REQUEST_URL + url,
            {
                headers: {
                    'Content-Type': 'application/json',
                    // 'Authorization': sessionStorage.getItem("Authorization"),
                },
                ...option,
            });
        if (res.status !== 200) {
            // alert(data.message);
            throw new Error(res);
        }
        /*
        if(url==='/api/auth/v0/login'){
            sessionStorage.setItem("Authorization",res.headers.get("Authorization"));
        }
        */
        return await res.json();
    } catch (error) {
        throw new Error(error);
    }
}

const fetchFileData = async (url,option) => {
    try{
        const res = await fetch(REQUEST_URL + url,
            {
                headers: {
                    // 'Authorization': sessionStorage.getItem("Authorization"),
                },
                ...option,
            });
        const data = await res.json();
        if (res.status !== 200) {
            // alert(data.message);
            throw new Error();
        }
        return data;
    }catch (error){
        throw new Error();
    }
}

const needAuth = () => {
    const auth = sessionStorage.getItem("Authorization");
    if(auth==null){
        location.href = location.origin + `/login`;
    }
}

const getLink = () => {
    return location.pathname.split('/')[2];
}

const hasLastChar = (content) => {
    const lastWord = content[content.length-1];
    const unicode = lastWord.charCodeAt();
    return (unicode - 0xAC00) % 28;
}