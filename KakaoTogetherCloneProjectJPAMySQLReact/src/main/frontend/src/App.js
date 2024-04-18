import './App.css';
import axios from 'axios';
import React, { useState, useEffect } from'react';


function App() {
  const [data, setData] = useState([]);

  useEffect(() => {
    axios.get('/test2')
    .then(res => {
        setData(res.data);
    })
    .catch(err => {
      console.log(err);
    });
  }, []);

  return (
    <div className="App">
      <header className="App-header">
        <p>
          Edit <code>src/App.js</code> and save to reload. {data}
          asfasfasfasf <br/>
          asfasf
          asfasfasfasf
        </p>
      </header>
    </div>
  );
}

export default App;
