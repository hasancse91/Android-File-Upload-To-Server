<?php
    $target_dir = "files/"; //folder name where your files will be stored. create this folder inside "file_upload_api" folder
    $target_file = $target_dir . basename($_FILES["file"]["name"]);

    $response = array('success' => false, 'message' => 'Sorry, there was an error uploading your file.');

    $data = $_POST['sender_information'];
    $json_data = json_decode($data , true);
    $sender_name = $json_data['sender_name'];
    $sender_age = $json_data['sender_age'];


    if (move_uploaded_file($_FILES["file"]["tmp_name"], $target_file))
        $response = array('success' => true, 'message' => 'Hello '.$sender_name.'! You are '.$sender_age.' years old. Your image is uploaded successfully!');

    echo json_encode($response);
?>
