// ChatbotPopup.js
import React from 'react';
import ChatbotTest from "./chatbot";
const overlayStyle = {
    position: 'fixed',
    bottom: 100,
    right: 20,
    width: 320,
    height: 400,
    backgroundColor: 'white',
    border: '1px solid #ccc',
    borderRadius: 8,
    boxShadow: '0 8px 16px rgba(0,0,0,0.2)',
    zIndex: 1001,
    display: 'flex',
    flexDirection: 'column',
};

const headerStyle = {
    padding: 10,
    borderBottom: '1px solid #eee',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
};

const closeBtnStyle = {
    cursor: 'pointer',
    border: 'none',
    background: 'none',
    fontSize: 20,
};

function ChatbotPopup({ onClose }) {
    return (
        <div style={overlayStyle}>
            <div style={headerStyle}>
                <button style={closeBtnStyle} onClick={onClose}>
                    ×
                </button>
            </div>
            <div style={{ padding: 10, flex: 1 }}>
                <ChatbotTest />
            </div>

        </div>
    );
}

export default ChatbotPopup;
