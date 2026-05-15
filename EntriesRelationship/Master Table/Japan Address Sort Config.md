# GWH_TM_JASC
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
| 18 | JASC_REC_KND | CHAR(2) | N | Record Kind / 業種 |
| 19 | JASC_SRADR_NAM | NVARCHAR2(400) | N | Search Address Name / 検索用住所名称 |
| 20 | JASC_SRADR_BYT | NUMBER(3) | N | Search Address Byte / 検索用住所名称有効バイト数 |
| 21 | JASC_DVBR_COD | NVARCHAR2(12) | N | Delivery Branch Code / 配達店コ－ド |
| 22 | JASC_SRT_COD | NVARCHAR2(14) | N | Sorting Code / 仕分番号 |
| 23 | JASC_PRF_COD | NVARCHAR2(4) | N | Prefecture Code / 都道府県コ－ド |
| 24 | JASC_CTY_COD | NVARCHAR2(6) | N | City Code / 市区町村コ－ド |
| 25 | JASC_SADR_COD | NVARCHAR2(6) | N | Subaddress Code / 補助住所コ－ド |
| 26 | JASC_CHM_COD | NVARCHAR2(6) | N | Chome Code / 丁目コ－ド |
| 27 | JASC_DSEX_KND | CHAR(1) | N | District Extra Kind / 地区割増区分 |
| 28 | JASC_WTEX_KND | CHAR(1) | N | Winter Extra Kind / 冬期割増区分 |
| 29 | JASC_RMT_KND | CHAR(1) | N | Remote Island Kind / 離島区分 |
| 30 | JASC_DVDT_WAY | NUMBER(3) | Y | Delivery Distance Way / 配達距離km |
| 31 | JASC_DLV_KND | CHAR(1) | N | Delivery Kind / 配達可否区分 |
