package com.wangin.admin.controller;

import com.wangin.admin.dto.GolfDto;
import com.wangin.admin.dto.GolfUserDto;
import com.wangin.admin.repository.GolfRepository;
import com.wangin.admin.service.GolfService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Controller
@AllArgsConstructor
@RequestMapping("/")
public class GolfController {

    private GolfService golfService;

    private GolfRepository golfRepository;

    @GetMapping("/")
    public String main(){
        return "golfchoice";
    }

    @GetMapping("/gjoin")
    public String gjoin(){
        return "golfjoin";
    }

    @GetMapping("/glogin")
    public String glogin(){
        return "golfchoice";
    }

    @GetMapping("/gaccount")
    public String gaccount(){
        return "golfchoice";
    }

    @GetMapping("/gcountryclub")
    public String gcountryclub(){
        return "golfchoice";
    }

    @GetMapping("/greservationi")
    public String greservationi(){
        return "golfclupinsert";
    }

    @GetMapping("/greservations")
    public String greservations(){
        return "golfchoice";
    }

    @PostMapping("/golftest2")
    public String golftest2(HttpServletRequest request, Model model, @RequestParam(required = false, defaultValue = "", value = "uid") String uid, @RequestParam(required = false, defaultValue = "", value = "upw") String upw, String... args){
        String ipmsg = uid + "/" + upw;
        System.out.println(ipmsg);
        // ??????????????? ????????? ?????? threadpool??? ????????????. ????????? ???????????? ?????? 10?????? ???????????? ???????????? ??? ??????.
        ExecutorService clientService = Executors.newFixedThreadPool(10);
        // serverSocket??? ????????????.
        try (ServerSocket server = new ServerSocket()) {
            System.out.println("???????????? ??????");
            // ????????? 9513?????? ????????????.
            InetSocketAddress ipep = new InetSocketAddress(9513);
            server.bind(ipep);
            clientService.submit(() -> {
                try{
                    String arg1;
                    ProcessBuilder builder;
                    BufferedReader br;

                    arg1 = "C:/Users/abcd/Desktop/selenium/selenium/golftest4.py";
                    System.out.println("?????? ????????????????");
                    builder = new ProcessBuilder("python", arg1);

                    builder.redirectErrorStream(true);
                    Process process = builder.start();

                    int exitval = process.waitFor();

                    br = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));

                    String line;
                    while ((line = br.readLine()) != null){
                        System.out.println(">>> " + line);
                    }

                    if(exitval != 0){
                        System.out.println("???????????????");
                    }
                } catch (Throwable e){
                    e.printStackTrace();
                }
            });

            while (true) {
                System.out.println("????????? ????????????");
                // ?????????????????? ????????? ????????? ????????????.
                Socket client = server.accept();
                System.out.println("1");
                // ?????????????????? ????????? ?????? ????????? ?????? ???????????? ?????? ???????????? inputstream??? outputstream??? ?????????.
                clientService.submit(() -> {
                    try (OutputStream sender = client.getOutputStream();
                         InputStream receiver = client.getInputStream();) {
                        System.out.println("2");
                        // ?????? ?????? ????????? ??????.
                        while (true) {
                            System.out.println("????????? ????????????2");
                            byte[] data = new byte[4];
                            // ????????? ????????? ?????????.
                            receiver.read(data, 0, 4);
                            // ByteBuffer??? ?????? little ????????? ???????????? ????????? ????????? ?????????.
                            ByteBuffer b = ByteBuffer.wrap(data);
                            b.order(ByteOrder.LITTLE_ENDIAN);
                            int length = b.getInt();
                            // ???????????? ?????? ????????? ????????????.
                            data = new byte[length];
                            // ???????????? ?????????.
                            receiver.read(data, 0, length);

                            // byte????????? ???????????? string???????????? ????????????.
                            String msg = new String(data, "UTF-8");
                            // ????????? ????????????.
                            System.out.println(msg);
                            // echo??? ?????????.
                            msg = ipmsg;
                            // string??? byte?????? ???????????? ????????????.
                            data = msg.getBytes();
                            // ByteBuffer??? ?????? ????????? ????????? byte???????????? ????????????.
                            b = ByteBuffer.allocate(4);
                            // byte????????? little ???????????????.
                            b.order(ByteOrder.LITTLE_ENDIAN);
                            b.putInt(data.length);
                            // ????????? ?????? ??????
                            sender.write(b.array(), 0, 4);
                            // ????????? ??????
                            sender.write(data);
                            server.close();
                            break;
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            // ????????? ???????????? ????????? ????????????.
                            client.close();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return "golfchoice";
    }

    @PostMapping("/golftest3")
    public String golftest3(HttpServletRequest request, Model model,
                            @RequestParam(required = false, defaultValue = "", value = "mountin") int mountin,
                            @RequestParam(required = false, defaultValue = "", value = "hope_y") int hope_y,
                            @RequestParam(required = false, defaultValue = "", value = "hope_m") int hope_m,
                            @RequestParam(required = false, defaultValue = "", value = "hope_d") int hope_d,
                            @RequestParam(required = false, defaultValue = "", value = "hope_c") int hope_c,
                            @RequestParam(required = false, defaultValue = "", value = "hope_t1") int hope_t1,
                            @RequestParam(required = false, defaultValue = "", value = "hope_t2") int hope_t2,
                            @RequestParam(required = false, defaultValue = "", value = "hope_h") int hope_h,
                            @RequestParam(required = false, defaultValue = "", value = "user_id") String user_id,
                            @RequestParam(required = false, defaultValue = "", value = "user_pw") String user_pw){
        GolfDto golfDto = new GolfDto(null, 0, 0, mountin, user_id, user_pw, hope_y, hope_m, hope_d, hope_t1, hope_t2, hope_h, hope_c, 0, 0, 0, 1, null, null, null);
        golfService.insertData1(golfDto);
        return "golfclupinsert";
    }

    @PostMapping("/golftest4")
    public String golftest4(HttpServletRequest request, Model model,
                            @RequestParam(required = false, defaultValue = "", value = "uid") String uid,
                            @RequestParam(required = false, defaultValue = "", value = "upw") String upw,
                            @RequestParam(required = false, defaultValue = "", value = "uname") String uname,
                            @RequestParam(required = false, defaultValue = "", value = "uphone") String uphone,
                            @RequestParam(required = false, defaultValue = "", value = "sms") int sms){
        System.out.println("???????");
        GolfUserDto golfUserDto = new GolfUserDto(null, uid, upw, uname, uphone, sms, 0, null,null,null);
        golfService.insertData2(golfUserDto);
        return "golfclupinsert";
    }

}
