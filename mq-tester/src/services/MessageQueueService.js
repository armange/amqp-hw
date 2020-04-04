import axios from 'axios';

export default {
    name: 'MessageQueueService',
    getMessage(queue, success, fail) {
        axios
            .get('http://localhost:4321/amqp/'+queue+'/receive', 
            {
                auth: { 
                   username: 'user', 
                   password: '123456' 
                }
             })
            .then(success)
            .catch(fail)
    },
    sendMessage(queue, message, success, fail) {
        axios
            .post('http://localhost:4321/amqp/'+queue+'/send', 
            message 
            , {
                headers: { 'Content-Type': 'text/plain' },
                auth: { 
                   username: 'user', 
                   password: '123456' 
                }
             })
            .then(success)
            .catch(fail)
    }
}