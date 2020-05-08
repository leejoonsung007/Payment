<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Pay</title>
</head>
<body>
    <div id="qrcodeCanvas"></div>
    <div id="orderId" hidden>${orderId}</div>
    <div id="redirectUrl" hidden>${redirectUrl}</div>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
    <script>
        jQuery('#qrcodeCanvas').qrcode({
            text : "${codeUrl}"
        });

        $(function() {
            // timer
            setInterval(function() {
                console.log('start to check the status of order..')
                $.ajax({
                    url: '/payment/queryByOrderId',
                    data: {
                        'orderId': $('#orderId').text()
                    },
                    success: function(result) {
                        console.log(result);
                        if (result.platformStatus != null
                            && result.platformStatus === 'success') {
                            location.href = $('#redirectUrl').text()
                        }
                    },
                    error: function (result) {
                        alert(result);
                    }
                })
            }, 5000)
        })
    </script>
</body>
</html>
