# GWH_TM_JZSC
#Entity #Standard #MASTER

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(200) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(200) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | JZSC_REC_KND | CHAR(2) | N | Record Kind / 業種 |
| 19 | JZSC_ZIP_COD | NVARCHAR2(14) | N | Zip Code / 郵便番号 |
| 20 | JZSC_STR_COD | NVARCHAR2(12) | N | Store Code / 店コ－ド |
| 21 | JZSC_SRT_COD | NVARCHAR2(14) | N | Sorting Code / 仕分番号 |
| 22 | JZSC_PRF_COD | NVARCHAR2(4) | N | Prefecture Code / 都道府県コ－ド |
| 23 | JZSC_CTY_COD | NVARCHAR2(6) | N | City Code / 市区町村コ－ド |
| 24 | JZSC_SADR_COD | NVARCHAR2(6) | N | Subaddress Code / 補助住所コ－ド |
| 25 | JZSC_CHM_COD | NVARCHAR2(6) | N | Chome Code / 丁目コ－ド |
| 26 | JZSC_DSEX_KND | CHAR(1) | N | District Extra Kind / 地区割増区分 |
| 27 | JZSC_WTEX_KND | CHAR(1) | N | Winter Extra Kind / 冬期割増区分 |
| 28 | JZSC_RMT_KND | CHAR(1) | N | Remote Island Kind / 離島区分 |
| 29 | JZSC_DVDT_WAY | NUMBER(3) | Y | Delivery Distance Way / 配達距離km |
| 30 | JZSC_DLV_KND | CHAR(1) | N | Delivery Kind / 配達可否区分 |
