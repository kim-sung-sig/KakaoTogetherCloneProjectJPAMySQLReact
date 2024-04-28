import './App.css';
import axios from 'axios';
import React, { useState, useEffect } from 'react';


function App() {
    const [data, setData] = useState([]);
    useEffect(() => {
        axios.get('/test')
            .then(res => {
                setData(res.data);
            })
            .catch(err => {
                console.log(err);
            });
    }, []);

    const onNaverLogin = () => {
        window.location.href = "http://localhost:8080/oauth2/authorization/naver";
    }

    return (
        <div className="App">
            <header className="App-header">
                <p>
                    Edit <code>src/App.js</code> and save to2 reload. <br /> {data}<br />
                    <button className='uk-button' onClick={onNaverLogin}>네이버 로그인</button><br />
                </p>
            </header>
        </div>
    );
}

export default App;
