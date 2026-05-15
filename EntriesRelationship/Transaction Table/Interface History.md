# GWH_TJ_IF_HIS
#Entity #Standard #HISTORY

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
| 18 | IFH_ID | NVARCHAR2(100) | N | Linkage ID / 連携ID |
| 19 | IFH_SEQ_NUM | NUMBER(38) | N | Process SEQ number / 処理SEQ番号 |
| 20 | IFH_TRGT_ID | NVARCHAR2(30) | Y | Process target ID / 処理対象ID |
| 21 | IFH_STRT_YMDHMS | TIMESTAMP(6) | Y | Process start time / 処理開始時刻 |
| 22 | IFH_END_YMDHMS | TIMESTAMP(6) | Y | Process end time / 処理終了時刻 |
| 23 | IFH_PROC_QTY | NUMBER(10) | Y | Number of process / 処理件数 |
| 24 | IFH_SUCS_QTY | NUMBER(10) | Y | Numver of success / 正常処理件数 |
| 25 | IFH_WRNG_QTY | NUMBER(10) | Y | Number of warning / 警告件数 |
| 26 | IFH_ERR_QTY | NUMBER(10) | Y | Number of error / エラー件数 |
| 27 | IFH_PROC_STS | NVARCHAR2(20) | Y | Process satus / 処理ステータス |
| 28 | IFH_LKFR_RMKS | BLOB | Y | Linkage form data remarks / 連携元処理データ内容 |
| 29 | IFH_LKTO_RMKS | BLOB | Y | Linkage to data remarks / 連携先処理データ内容 |
| 30 | IFH_ERR_RMKS | BLOB | Y | Error detail / エラー詳細 |
