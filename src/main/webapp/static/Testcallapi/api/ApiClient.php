<?php

class ApiClient {

    public $apiUrl = "http://beta.chodientu.vn/app/user/signin.api";
    private $username = "store247";
    private $password = "123456";

    public function call() {
        $url = $this->apiUrl;
        $requestBody = new ArrayObject();
        $requestBody["username"] = $this->username;
        $requestBody["password"] = $this->password;
        $json = @json_encode($requestBody);
        $ch = curl_init();
        // $headers = array(
        //     'Authorization:Basic Y2hvZGllbnR1bW9iaWxlYXBw'
        // );
        
        // curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        //header('Authorization:Basic Y2hvZGllbnR1bW9iaWxlYXBw');
        foreach (getallheaders() as $name => $value) {
            echo "$name: $value\n";
        }
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_VERBOSE, 1);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_ENCODING, 'UTF-8');
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json','Authorization:Basic Y2hvZGllbnR1bW9iaWxlYXBw'));
        $result = curl_exec($ch);
        curl_close($ch);
        $rs = json_decode($result, false);
        return $rs;
    }

//    public function postItem($data) {
//        $item = $this->call("item/postitem", $data);
//if($item->success) {  echo "<p>- success -id :".$item->data->id.": Link: ".$item->data->sellerSku.": </p>"."start-price".$item->data->startPrice.
//"sell-price".$item->data->sellPrice;  } else 
//{  echo "<p> - Fail ".$data['sellerSku']."<pre>". print_r($item->data) ."</pre></p>";  }
//    }

    public function postItem() {
        $item = $this->call();
         
            echo @json_encode($item);  
        
        //  else {  
        //     echo "<p> - Fail ".$data['sellerSku']."<pre>". print_r($item->data) ."</pre></p>";  
        // }
    }
}