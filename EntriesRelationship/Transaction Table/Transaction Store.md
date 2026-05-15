# GWH_TJ_XT_STR
#Entity #Standard #TRANSACTION_LOG

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
| 18 | STR_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | STR_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | STR_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | STR_LGSQ_NUM | NUMBER(4) | N | Log Seq Number / ログ用シーケンス |
| 22 | STR_WK_GRP | CHAR(2) | N | Work Group / 作業グループ |
| 23 | STR_WK_KND | CHAR(2) | N | Work Kind / 作業区分 |
| 24 | STR_WK_NAM | NVARCHAR2(100) | Y | Operation Name / 作業内容名称 |
| 25 | STR_OP_YMD | CHAR(8) | N | Start Date / 開始日 |
| 26 | STR_OP_TIM | CHAR(6) | N | Start HMS / 開始時刻 |
| 27 | STR_OP_USER | NVARCHAR2(100) | N | Start User ID / 開始担当者コード |
| 28 | STR_OP_NUM | NVARCHAR2(12) | N | Arrival and Shipping Number / 処理番号 |
| 29 | STR_OPLN_NUM | NUMBER(4) | N | Arrival and Shipping Line Number / 処理ラインNo |
| 30 | STR_OPSQ_NUM | NUMBER(4) | N | Arrival and Shipping Seq Number / 処理SEQNo |
| 31 | STR_AS_NUM | NVARCHAR2(12) | N | Arrival and Shipping Number / 入出荷番号 |
| 32 | STR_ASLN_NUM | NUMBER(4) | N | Arrival and Shipping Line Number / 入出荷ラインNo |
| 33 | STR_ASSQ_NUM | NUMBER(4) | N | Arrival and Shipping Seq Number / 入出荷SEQNo |
| 34 | STR_TP_NUM | NVARCHAR2(12) | Y | Total Picking Number / トータルピッキング番号 |
