package com.example.finalapp.dto.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Slice<T> {
    boolean hasNext;
    List<T> contentList;
}
















