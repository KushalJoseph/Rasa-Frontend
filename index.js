const express = require('express');
const app = express();
const axios = require('axios');
app.use(express.json());


app.post('/', (req, res) => {
	console.log(req.body);

	let bodyForRasa = {
		"sid":"3",
		"message":req.body.message,
		"lang":req.body.lang
	}

	axios.post("https://938ee7a4b9c0.ngrok.io", bodyForRasa)
		.then(response => {
			let text = "";
			const intent = response.data.intent.name;
			if(intent == "goodbye") text = "Custom Goodbye message for you :)";
			else if(intent == "mood_unhappy") text = "Custom cheer up. Just chill :)";
			else if(intent == "greet") text = "Well, well, well, hello there";
			else if(intent == "goodbye") text = "See ya later!";
			else text = "sorry, i didnt catch that, please speak again";
			res.send({
				"text":text
			});
		})
		.catch(error => {
			console.log(error);
			res.send({"error":"1"});
		});
});


app.listen(8080, () => {
	console.log("Server is up and running");
})
