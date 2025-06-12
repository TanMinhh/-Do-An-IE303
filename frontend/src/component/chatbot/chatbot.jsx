import React, { useState } from "react";

function ChatbotTest() {
    const [prompt, setPrompt] = useState("");
    const [response, setResponse] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError("");
        setResponse("");

        try {
            const res = await fetch("http://localhost:8080/chatbot/ask", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify({ prompt }),
            });

            if (!res.ok) {
                const errData = await res.json();
                throw new Error(errData.message || "Error occur calling API");
            }

            const data = await res.json();

            // Lấy đúng phần text từ JSON
            const answer = data.candidates?.[0]?.content?.parts?.[0]?.text || "No answer found";

            setResponse(answer);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };



    return (
        <div style={{ maxWidth: 600, margin: "auto", padding: 20 }}>
            <h2>Xpress Chatbot</h2>
            <form onSubmit={handleSubmit}>
                <textarea
                    rows={4}
                    placeholder="Prompting..."
                    value={prompt}
                    onChange={(e) => setPrompt(e.target.value)}
                    style={{ width: "96%", fontSize: 16, padding: 10 }}
                />
                <button type="submit" disabled={loading || !prompt.trim()} style={{ marginLeft: 12 }}>
                    {loading ? "Sending..." : "Send"}
                </button>
            </form>
            {error && <p style={{ color: "red" }}>Error: {error}</p>}
            {response && (
                <pre style={{ fontFamily: "monospace", background: "#eee", padding: 10, marginTop: 20, whiteSpace: "pre-wrap" }}>
                    {response}
                </pre>
            )}
        </div>
    );
}

export default ChatbotTest;