const STORE_NAME = "user";

/**
 * Get the Object that is in the localStorage under the storeName key
 * @param {string} storeName
 * @returns
 */
const getSessionObject = (storeName) => {
  var retrievedObject = localStorage.getItem(storeName);
  if (!retrievedObject) {
    retrievedObject = sessionStorage.getItem(storeName);
  }
  return JSON.parse(retrievedObject);
};

const setSession = (storeName, object) => {
  var retrievedObject = localStorage.getItem(storeName);
  if (!retrievedObject) {
    sessionStorage.removeItem(storeName);
    const storageValue = JSON.stringify(object);
    sessionStorage.setItem(storeName, storageValue);
  } else {
    localStorage.removeItem(storeName);
    const storageValue = JSON.stringify(object);
    localStorage.setItem(storeName, storageValue);
  }
}

//get the user from the session
const getSessionData = (storeName) => {
  const retrievedObject = sessionStorage.getItem(storeName);
  if (!retrievedObject) {
    return;
  }
  return JSON.parse(retrievedObject);
};

/**
 * Set the object in the localStorage under the storeName key
 * @param {string} storeName
 * @param {Object} object
 */
const setSessionObject = (storeName, object) => {
  const storageValue = JSON.stringify(object);
  localStorage.setItem(storeName, storageValue);
};

/**
 * Remove the object in the localStorage under the storeName key
 * @param {String} storeName
 */
const removeSessionObject = (storeName) => {
  localStorage.removeItem(storeName);
};

const removeLocalData = () => {
  localStorage.removeItem(STORE_NAME);
  sessionStorage.removeItem(STORE_NAME);
};

const setSessionData = (storeName, object) => {
  const storageValue = JSON.stringify(object);
  sessionStorage.setItem(storeName, storageValue);
};

export {
  getSessionObject,
  setSessionObject,
  removeSessionObject,
  removeLocalData,
  setSessionData,
  getSessionData,
  setSession
};