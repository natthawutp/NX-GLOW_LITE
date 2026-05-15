# GWH_TJ_OPE_HIS
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
| 18 | OPEH_SEQ | NUMBER(17) | N | Sequence / 処理SEQ |
| 19 | OPEH_NCUL_COD | NUMBER(15) | Y | Login ID (NCU) / Login ID (NCU) |
| 20 | OPEH_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 21 | OPEH_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 22 | OPEH_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 23 | OPEH_USER_COD | NVARCHAR2(100) | N | User ID / ユーザID |
| 24 | OPEH_STR_YMDHMS | TIMESTAMP(6) | N | Start Timestamp / 開始タイムスタンプ |
| 25 | OPEH_END_YMDHMS | TIMESTAMP(6) | N | End Timestamp / 終了タイムスタンプ |
| 26 | OPEH_SCN_COD | NVARCHAR2(60) | N | Screen ID / 画面ID |
| 27 | OPEH_FUNC_COD | NVARCHAR2(200) | N | Function ID / 機能ID |
| 28 | OPEH_EVNT_KND | NVARCHAR2(10) | Y | Event Kind / イベント種別 |
| 29 | OPEH_ERR_FLG | CHAR(1) | N | ERROR Flag / ERRORフラグ |
| 30 | OPEH_MSG_FIL | NVARCHAR2(1000) | Y | Message / メッセージ内容 |
| 31 | OPEH_SSN_COD | NVARCHAR2(400) | Y | Session Key / セッションキー |
