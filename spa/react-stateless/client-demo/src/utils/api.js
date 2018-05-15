import axios from 'axios';

const BASE_URL = 'https://localhost:8443';

export {getPetsData};

function getPetsData(csrf) {
  console.log("csrf in getPetsData = " + csrf);
  const url = `${BASE_URL}/v1/pets`;
  // set this as header globally
  axios.defaults.headers.common['X-CSRF-TOKEN'] = csrf;
  // withCredentials true means the browser will send the cookies. 
  return axios.get(url, {withCredentials: true}).then(response => response.data);
}
