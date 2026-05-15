# GWH_TJ_EE_HIS
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
| 18 | EE_HIS_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | EE_HIS_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | EE_HIS_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | EE_HIS_SR_NUM | NVARCHAR2(60) | N | Send/Receive Number / 送受信NO |
| 22 | EE_HIS_SRSQ_NUM | NUMBER(4) | N | Send/Receive Processing Seq / 処理シーケンス |
| 23 | EE_HIS_LINE_NUM | NUMBER(7) | N | File Line Number / ファイル行番号 |
| 24 | EE_HIS_ERR_NUM | NUMBER(7) | N | File Error Number / エラーシーケンス |
| 25 | EE_HIS_SR_YMD | CHAR(8) | N | Send/Receive Date / 送受信日 |
| 26 | EE_HIS_SR_TIM | CHAR(6) | Y | Send/Receive Time / 送受信時間 |
| 27 | EE_HIS_DTKD_COD | CHAR(2) | N | Date Kind Code / データ種別コード |
| 28 | EE_HIS_IMP_NAM | NVARCHAR2(400) | Y | Import Item Name 01 / 取込項目名称01 |
| 29 | EE_HIS_IMP_DT | NVARCHAR2(1200) | Y | Import Item Data 01 / 取込項目データ01 |
| 30 | EE_HIS_REMG_ID | CHAR(18) | Y | Receive Error Message ID / 受信エラーメッセージID |
| 31 | EE_HIS_RERR_RMKS | NVARCHAR2(400) | Y | Receive Error Remarks / 受信エラー内容 |
