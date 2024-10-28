**Real-Time Streaming By HTTP2 Stream (instead of WebSocket) Using Java, Spring Boot and Kafka**




1. First of all install kafka and kafka ui (https://raw.githubusercontent.com/Jahadul-Rakib/http2-stream-using-java-spring-boot-webflux/refs/heads/master/kafka/docker-compose.yaml)
2. implement http2 stream using webflux, reactor with kafka.
3. check by postman client or javascript.
4. Integrate in Front-end or anywhere else.
5. Enjoy...

Here added sample code to implement using react and interact with backend service over Http2 Stream:

    
    import {useEffect, useState} from "react";

    export default function StreamComponent(topic: { topic: string }) {

    const [messages, setMessages] = useState<string[]>([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const eventSource = new EventSource(`http://localhost:8080/api/v1/stream?topic=${topic.topic}`);

        eventSource.onmessage = (event) => {
            setMessages((prevMessages: any) => [...prevMessages, event.data]);
        };

        eventSource.onerror = (err) => {
            setError('An error occurred while streaming data.');
            console.error('EventSource failed:', err);
            eventSource.close();
        };

        return () => {
            eventSource.close();
        };
    }, [topic]);

    return (
        <section>
            <h2>Messages from Topic: {topic.topic}</h2>
            {error && <p style={{color: 'red'}}>{error}</p>}
            <p style={{color: 'green', overflowY: 'auto', height: '100vh'}}>
                {messages.map((msg, index) => (
                    <span key={index}>{msg} <br/></span>
                ))}
            </p>
        </section>
        );
    }




