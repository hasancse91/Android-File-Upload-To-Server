<?php
    $target_dir = "files/"; //folder name
    $target_file = $target_dir . basename($_FILES["fileToUpload"]["name"]);
    $response = array('success' => false, 'message' => 'Sorry, there was an error uploading your file.');

    if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file))
        $response = array('success' => true, 'message' => 'Image upload successful!');

    echo json_encode($response);
?>