# GWH_TJ_EDI_H
#Entity #Standard #EDI

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
| 18 | EDIH_PRC_NUM | NVARCHAR2(60) | N | Processing Seq / 処理No. |
| 19 | EDIH_PRC_KND | CHAR(2) | N | Processing Kind / 処理区分 |
| 20 | EDIH_PRC_TIM | TIMESTAMP(6) | N | Processing Time / 処理指示時間 |
| 21 | EDIH_PRC_STS | CHAR(1) | N | Processing Status / ステータス |
| 22 | EDIH_DAT_KND | CHAR(2) | N | Data Kind / データ種別 |
| 23 | EDIH_HOST_NAM | NVARCHAR2(136) | N | Host Name / ホスト名 |
| 24 | EDIH_CUST_COD | NVARCHAR2(100) | N | EDI Customer Code / EDI顧客コード |
| 25 | EDIH_PRM1 | NVARCHAR2(60) | Y | EDI Parameter1 / パラメータ１ |
| 26 | EDIH_PRM2 | NVARCHAR2(60) | Y | EDI Parameter2 / パラメータ２ |
| 27 | EDIH_PRM3 | NVARCHAR2(60) | Y | EDI Parameter3 / パラメータ３ |
| 28 | EDIH_PRM4 | NVARCHAR2(60) | Y | EDI Parameter4 / パラメータ４ |
| 29 | EDIH_PRM5 | NVARCHAR2(60) | Y | EDI Parameter5 / パラメータ５ |
| 30 | EDIH_STAT_TIM | TIMESTAMP(6) | N | Start Time / 開始時間 |
| 31 | EDIH_END_TIM | TIMESTAMP(6) | Y | End Time / 終了時間 |
| 32 | EDIH_PRC_CNT | NUMBER(8) | Y | Processing Count / 処理件数 |
| 33 | EDIH_SND_TIME | TIMESTAMP(6) | Y | Send Instruction Time / 送信側指示時間 |
