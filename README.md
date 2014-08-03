English-Tamil-OCR-Translation
=============================

English OCR with translation to Tamil Unicode.

This is an Android app which allows the users to capture an image or choose one from the gallery then converts all 
textual data in the image in English, to Tamil unicode text and displays it to the users.


This is to help Tamilians (people who speak Tamil) who have insufficient understanding of English to be able to communicate better by helping them transalate the world around them.

===========================

Two separate REST API's have been used to be able to acheive this

OCR:

The REST api provided by http://ocrapiservice.com/ is used for English OCR.

The output text is then fed into the Tamil translator

English to Tamil Tranlator:

Api provided by http://mymemory.translated.net/doc/spec.php is used for the translation.

==========================

Installation:

The OCR service requires a API key which will be provided to you upon registration on their site.
Fill this key in the apiKey variable in MainActivity.java and proceed to compile and run the project.

---
If you just want a short demo of the application (just a quick look), install the apk found in bin directory. It has my api key built in.

Both the APIs ive used have a free rate limit per day. If exceeded, the app wont work.


