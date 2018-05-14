import axios from 'axios';

const BASE_URL = 'https://localhost:8443';

export {getPetsData};

function getPetsData() {
  const url = `${BASE_URL}/v1/pets`;
  return axios.get(url).then(response => response.data);
}
