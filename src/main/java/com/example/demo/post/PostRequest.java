package com.example.demo.post;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class PostRequest {

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Schema(description = "제목 입력", example = "제목입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Schema(description = "내용 입력", example = "내용입니다.")
    private String content;

    private String code;


}
