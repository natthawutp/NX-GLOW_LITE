# GWH_TJ_XT_KPI
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
| 18 | KPI_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | KPI_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | KPI_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | KPI_LGSQ_NUM | NVARCHAR2(40) | N | Log Seq Number / ログ用シーケンス |
| 22 | KPI_AS_KND | NVARCHAR2(4) | N | Arrival and Shipping Kind / 入出荷区分 |
| 23 | KPI_OP_KND | NVARCHAR2(4) | N | Operation Kind / 作業区分 |
| 24 | KPI_OP_COD | NVARCHAR2(6) | N | Operation Code / 作業内容コード |
| 25 | KPI_OP_NAM | NVARCHAR2(100) | N | Operation Name / 作業内容名称 |
| 26 | KPI_OP_YMD | CHAR(8) | N | Operation Date / 作業日 |
| 27 | KPI_OP_TIM | CHAR(6) | N | Operation HMS / 作業時刻 |
| 28 | KPI_OP_USER | NVARCHAR2(80) | N | Operate User ID / 作業担当者コード |
| 29 | KPI_OP_TYPE | CHAR(1) | Y | Operation Type / 作業タイプ |
| 30 | KPI_AS_NUM | NVARCHAR2(12) | N | Arrival and Shipping Number / 処理番号 |
| 31 | KPI_ASLN_NUM | NUMBER(4) | N | Arrival and Shipping Line Number / 処理ラインNo |
| 32 | KPI_ASSQ_NUM | NUMBER(4) | N | Arrival and Shipping Seq Number / 処理SEQNo |
| 33 | KPI_AV_NUM | NVARCHAR2(12) | Y | Arrival Number / 入荷番号 |
| 34 | KPI_AVLN_NUM | NUMBER(4) | Y | Arrival Line No / 入荷ラインNo |
| 35 | KPI_AVSQ_NUM | NUMBER(4) | Y | Arrival Seq No / 入荷SEQNo |
