import './App.css';
import React, { useState, useEffect } from "react";

function App() {
    const [phoneEvents, setPhoneEvents] = useState([]);
    const [emailEvents, setEmailEvents] = useState([]);
    const [smsEvents, setSmsEvents] = useState([]);

    useEffect(() => {
        const phoneSource = new EventSource("http://localhost:9090/phone");
        const emailSource = new EventSource("http://localhost:9090/email");
        const smsSource = new EventSource("http://localhost:9090/sms");

        phoneSource.onmessage = (event) => {
            setPhoneEvents((prevEvents) => [...prevEvents, event.data]);
        };

        emailSource.onmessage = (event) => {
            setEmailEvents((prevEvents) => [...prevEvents, event.data]);
        };

        smsSource.onmessage = (event) => {
            setSmsEvents((prevEvents) => [...prevEvents, event.data]);
        };

        return () => {
            phoneSource.close();
            emailSource.close();
            smsSource.close();
        };
    }, []);

    return (
        <div className="App">
            <h1>Server-Sent Events</h1>
            <h2>Phone Events</h2>
            <ul id="phoneEvents">
                {phoneEvents.map((event, index) => (
                    <li key={index}>{event}</li>
                ))}
            </ul>
            <h2>Email Events</h2>
            <ul id="emailEvents">
                {emailEvents.map((event, index) => (
                    <li key={index}>{event}</li>
                ))}
            </ul>
            <h2>SMS Events</h2>
            <ul id="smsEvents">
                {smsEvents.map((event, index) => (
                    <li key={index}>{event}</li>
                ))}
            </ul>
        </div>
    );
}

export default App;
