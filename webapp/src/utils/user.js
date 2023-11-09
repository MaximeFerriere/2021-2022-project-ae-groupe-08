let isAdmin=false;
let idObject=0;


function getIsAdmin(){
    return isAdmin;
}

function setAdmin(){
    isAdmin=true;
}

function setAdminFalse(){
    isAdmin=false;
}

function setIdObject( id){
    idObject=id;
}

function getIdObject(){
    return idObject;
}

export {getIsAdmin, setAdmin, setAdminFalse,setIdObject, getIdObject};