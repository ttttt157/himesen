INSERT IGNORE INTO poll (id, question) VALUES 
	(1, 'What is your favorite programming language?');

INSERT IGNORE INTO choice (id, text, votes, poll_id) VALUES 
	(1, 'Java', 0, 1),
	(2, 'Python', 0, 1),
	(3, 'JavaScript', 0, 1),
	(4, 'C++', 0, 1);