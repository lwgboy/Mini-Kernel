package com.gcloud.api;

import com.gcloud.core.util.ApiDocUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
public class ApiDocController {
	@GetMapping("/apidoc")
	public void apiDoc(HttpServletResponse response){

		String fileName = "gcloud8_api.md";
		String markdownStr = ApiDocUtil.getInstance().getDoc();

		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		response.addHeader("Content-Type", "text/markdown; charset=UTF-8");

		try(OutputStream out = response.getOutputStream()){
			out.write(markdownStr.getBytes(StandardCharsets.UTF_8));
		}catch (Exception ex){
			log.error("write response error:" + ex, ex);
		}

	}
}
