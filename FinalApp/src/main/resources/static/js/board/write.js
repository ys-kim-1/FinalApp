{
    let $input = document.querySelector('#post-image');
    let $imgList = document.querySelectorAll('.img-list');

    $input.addEventListener('change', function () {
        // console.dir(this)
        let files = this.files;

        let newFiles = checkLength(files, 4);
        appendImg(newFiles);
        this.files = newFiles;

        console.log(this.files);
    });

    $imgList.forEach(ele => {
        ele.addEventListener('click', function(){
            let name = this.dataset.name;
            // console.log(name);
            removeImg(name);

            appendImg($input.files);
        });
    });


    /**
     * 미리보기 삭제 함수
     *
     * @param name : string
     */
    function removeImg(name){
        let dt = new DataTransfer();
        let files = $input.files;

        for(let i=0; i<files.length; i++){
            if(files[i].name !== name){
                dt.items.add(files[i]);
            }
        }

        $input.files = dt.files;
    }



    /**
     * 이미지 미리보기 처리 함수
     * 이미지 수가 4개보다 적으면 기본 이미지로 대체
     *
     * @param files : FileList
     */
    function appendImg(files){
        for (let i = 0; i < 4; i++) {
            if(i < files.length){
                let src = URL.createObjectURL(files[i]);

                $imgList[i].style.background = `url(${src})`;
                $imgList[i].style.backgroundSize = 'cover';
                $imgList[i].dataset.name = files[i].name;
                $imgList[i].classList.add('x-box');
            }else {
                $imgList[i].style.background = `url(data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIzNiIgaGVpZ2h0PSIzNiI+PGcgZmlsbD0ibm9uZSIgZmlsbC1ydWxlPSJldmVub2RkIj48ZyBzdHJva2U9IiNCNUI1QjUiIHN0cm9rZS13aWR0aD0iMS41IiB0cmFuc2Zvcm09InRyYW5zbGF0ZSg0IDQpIj48cmVjdCB3aWR0aD0iMjgiIGhlaWdodD0iMjgiIHJ4PSIzLjUiLz48Y2lyY2xlIGN4PSI4LjU1NiIgY3k9IjguNTU2IiByPSIyLjMzMyIvPjxwYXRoIGQ9Ik0yOCAxOC42NjdsLTcuNzc3LTcuNzc4TDMuMTExIDI4Ii8+PC9nPjxwYXRoIGQ9Ik0wIDBoMzZ2MzZIMHoiLz48L2c+PC9zdmc+)
    no-repeat 50% #f2f2f2`;
                $imgList[i].style.backgroundSize = 'none';
                delete $imgList[i].dataset.name;
                $imgList[i].classList.remove('x-box');
            }
        }
    }



    /**
     * 파일 길이 체크 함수
     *
     * @param files :FileList fileList 객체
     * @param limit :number 최대 이미지 수
     * @returns {FileList|*} 결과 파일리스트 객체
     */
    function checkLength(files, limit){
        if(files.length > limit) {
            alert(`파일은 최대 ${limit}개 까지만 첨부 가능합니다.`);
            // 최대 수를 넘으면 비어있는 files 객체를 반환
            return new DataTransfer().files;
        }

        return files;
    }
}













