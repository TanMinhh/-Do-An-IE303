// ChatbotBubble.js
import React from 'react';

const style = {
    position: 'fixed',
    bottom: 20,
    right: 20,
    width: 60,
    height: 60,
    borderRadius: '50%',
    backgroundColor: '#007bff',
    color: 'white',
    cursor: 'pointer',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
    boxShadow: '0 4px 8px rgba(0,0,0,0.2)',
    zIndex: 1000,
};

function ChatbotBubble({ onClick }) {
    return (
        <div style={style} onClick={onClick} title="Chat with us!">
            💬
        </div>
    );
}

export default ChatbotBubble;