# GWH_TJ_MGTG
#Entity #Standard #MANAGEMENT

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(200) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(3) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(3) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(200) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(3) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(3) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | MGTG_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | MGTG_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | MGTG_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | MGTG_SEQ | NUMBER | N | SEQUENCE / シーケンス |
| 22 | MGTG_EXE_YDM | DATE | N | Execution Date / 実行日 |
| 23 | MGTG_STRT_TIM | CHAR(6) | N | Start Time / 開始時間 |
| 24 | MGTG_END_TIM | CHAR(6) | Y | End Time / 終了時間 |
| 25 | MGTG_DTCN_KND | CHAR(2) | N | Data Control Kind / 管理データ区分 |
| 26 | MGTG_EXE_ENV_COD | CHAR(2) | N | Execution Environment Code / 実行環境 |
| 27 | MGTG_SRC_ENV_COD | CHAR(2) | N | Source Environment Code / 移行元環境 |
| 28 | MGTG_DST_ENV_COD | CHAR(2) | N | Destination Environment Code / 移行先環境 |
| 29 | MGTG_SRC_CPNY_COD | NVARCHAR2(12) | N | Source Company Code / 移行元カンパニーコード |
| 30 | MGTG_SRC_WHS_COD | NVARCHAR2(8) | N | Source Warehouse Code / 移行元倉庫コード |
| 31 | MGTG_SRC_CUST_COD | NVARCHAR2(26) | N | Source Customer Code / 移行元顧客コード |
| 32 | MGTG_DST_CPNY_COD | NVARCHAR2(12) | N | Destination Company Code / 移行先カンパニーコード |
| 33 | MGTG_DST_WHS_COD | NVARCHAR2(8) | N | Destination Warehouse Code / 移行先倉庫コード |
| 34 | MGTG_DST_CUST_COD | NVARCHAR2(26) | N | Destination Customer Code / 移行先顧客コード |
| 35 | MGTG_PRFM_STS | CHAR(2) | N | Status / 実行ステータス |
| 36 | MGTG_PRFM_ID | NVARCHAR2(100) | N | Perfomer ID / 実行ユーザID |
| 37 | MGTG_ERR_MSG | NVARCHAR2(4000) | Y | Error Message / エラーメッセージ |
